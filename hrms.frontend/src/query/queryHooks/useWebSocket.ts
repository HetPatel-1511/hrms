import { useEffect, useRef, useState, useCallback } from 'react'
import SockJS from 'sockjs-client'
import { Client } from '@stomp/stompjs'
import { useQueryClient } from '@tanstack/react-query'
import { QUERY_KEY } from '../key'

interface UseWebSocketOptions {
    userId?: number | string
    autoConnect?: boolean
    onMessageReceived?: (message: any) => void
}

const useWebSocket = ({
    userId,
    autoConnect = true,
    onMessageReceived
}: UseWebSocketOptions = {}) => {
    const clientRef = useRef<Client | null>(null)
    const [isConnected, setIsConnected] = useState(false)
    const [error, setError] = useState<string | null>(null)
    const subscriptionRef = useRef<any>(null)
    const queryClient = useQueryClient()

    const connect = useCallback(() => {
        if (clientRef.current?.active) {
            return
        }

        try {
            const socket = new SockJS('http://localhost:8080/ws')
            const stompClient = new Client({
                webSocketFactory: () => socket,
                reconnectDelay: 5000,
                heartbeatIncoming: 4000,
                heartbeatOutgoing: 4000,
                onConnect: () => {
                    setIsConnected(true)
                    setError(null)

                    if (userId) {
                        subscriptionRef.current = stompClient.subscribe(
                            `/topic/notifications/${userId}`,
                            (message) => {
                                try {
                                    const notification = JSON.parse(message.body)
                                    queryClient.invalidateQueries({ queryKey: [QUERY_KEY.NOTIFICATIONS_UNREAD] })
                                    queryClient.invalidateQueries({ queryKey: [QUERY_KEY.NOTIFICATIONS] })
                                    if (onMessageReceived) {
                                        onMessageReceived(notification)
                                    }
                                } catch (err) {
                                    console.error('Error parsing notification:', err)
                                }
                            }
                        )
                    }

                    const allSubscription = stompClient.subscribe(
                        '/topic/notifications/all',
                        (message) => {
                            try {
                                const notification = JSON.parse(message.body)
                                queryClient.invalidateQueries({ queryKey: [QUERY_KEY.NOTIFICATIONS_UNREAD] })
                                queryClient.invalidateQueries({ queryKey: [QUERY_KEY.NOTIFICATIONS] })
                                if (onMessageReceived) {
                                    onMessageReceived(notification)
                                }
                            } catch (err) {
                                console.error('Error parsing notification:', err)
                            }
                        }
                    )

                    subscriptionRef.current = allSubscription
                },
                onDisconnect: () => {
                    setIsConnected(false)
                },
                onStompError: (frame) => {
                    setError(`STOMP error: ${frame.headers.message}`)
                    setIsConnected(false)
                    console.error('STOMP error:', frame)
                }
            })

            clientRef.current = stompClient
            stompClient.activate()
        } catch (err) {
            const errorMessage = err instanceof Error ? err.message : 'Failed to connect to WebSocket'
            setError(errorMessage)
            console.error('WebSocket connection error:', err)
        }
    }, [userId, queryClient, onMessageReceived])

    const disconnect = useCallback(() => {
        if (subscriptionRef.current) {
            subscriptionRef.current.unsubscribe()
            subscriptionRef.current = null
        }

        if (clientRef.current?.active) {
            clientRef.current.deactivate()
        }
        setIsConnected(false)
    }, [])

    const reconnect = useCallback(() => {
        console.log("aaaaaaaaaaaaaaa");
        
        disconnect()
        setTimeout(() => {
            console.log("1111111111111111111111111111");
            
            connect()
        }, 1000)
    }, [connect, disconnect])

    useEffect(() => {
        if (autoConnect) {
            console.log("222222222222222222222222");
            connect()
        }

        return () => {
            console.log("bbbbbbbbbbbbb");
            
            disconnect()
        }
    }, [autoConnect])

    return {
        isConnected,
        error,
        connect,
        disconnect,
        reconnect,
        client: clientRef.current
    }
}

export default useWebSocket
