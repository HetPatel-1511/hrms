import { useState } from 'react'
import { useParams, useNavigate } from 'react-router'
import useSingleGameQuery from '../query/queryHooks/useSingleGameQuery'
import useUpcomingSlotsQuery from '../query/queryHooks/useUpcomingSlotsQuery'
import useInterestedUsersQuery from '../query/queryHooks/useInterestedUsersQuery'
import useBookGameSlotMutation from '../query/queryHooks/useBookGameSlotMutation'
import Loading from '../components/Loading'
import ServerError from '../components/ServerError'
import Card from '../components/Card'
import Button from '../components/Button'
import UpcomingSlots from '../components/UpcomingSlots'
import InterestedUsers from '../components/InterestedUsers'

const SingleGame = () => {
    const { gameId } = useParams()
    const navigate = useNavigate()
    
    const [selectedSlot, setSelectedSlot] = useState<string | null>(null)
    const [selectedUsers, setSelectedUsers] = useState<string[]>([])
    
    const { data: gameData, isLoading: gameLoading, isError: gameError } = useSingleGameQuery(gameId!)
    const { data: slotsData, isLoading: slotsLoading } = useUpcomingSlotsQuery(gameId!)
    const { data: usersData, isLoading: usersLoading } = useInterestedUsersQuery(gameId!)
    const bookGameSlot = useBookGameSlotMutation()

    if (gameLoading || slotsLoading || usersLoading) {
        return <Loading />
    }
    if (gameError) {
        return <ServerError />
    }

    const game = gameData?.data
    const slots = slotsData?.data || []
    const interestedUsers = usersData?.data || []

    const handleSlotSelect = (slotId: string) => {
        setSelectedSlot(slotId === selectedSlot ? null : slotId)
        setSelectedUsers([])
    }

    const handleUserToggle = (userId: string) => {
        setSelectedUsers(prev => 
            prev.includes(userId) 
                ? prev.filter(id => id !== userId)
                : [...prev, userId]
        )
    }

    const handleBookSlot = () => {
        if (!selectedSlot || selectedUsers.length === 0) {
            alert('Please select a slot and at least one user')
            return
        }

        const bookingData = {
            gameSlotId: parseInt(selectedSlot),
            employeeIds: selectedUsers.map(id => parseInt(id))
        }

        bookGameSlot.mutate(bookingData)
    }

    const formatTime = (time: string) => {
        return new Date(`1970-01-01T${time}`).toLocaleTimeString('en-US', {
            hour: '2-digit',
            minute: '2-digit'
        })
    }

    const formatDate = (dateString: string) => {
        return new Date(dateString).toLocaleDateString('en-US', {
            weekday: 'short',
            month: 'short',
            day: 'numeric'
        })
    }

    return (
        <div>
            <div className="mb-6">
                <button 
                    onClick={() => navigate('/games')}
                    className="text-gray-600 hover:text-gray-800 text-sm font-medium"
                >
                    ← Back to Games
                </button>
            </div>

            <Card className="mb-6">
                <div className="px-6 py-4">
                    <div className="flex items-center justify-between">
                        <div>
                            <h1 className="text-2xl font-bold text-gray-900">{game?.name}
                                <span className={`px-3 py-1 text-xs font-medium rounded-full ml-2 ${
                                game.active ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                            }`}>
                                {game.active ? 'Active' : 'Inactive'}
                            </span>
                            </h1>
                            <p className="text-gray-600 mt-1">
                                Slot time: {game?.gameConfiguration && `${game.gameConfiguration.slotDurationMinutes}min`}
                            </p>
                        </div>
                        <div className="text-right">
                            <p className="text-sm text-gray-500">Operating Hours</p>
                            <p className="font-medium">
                                {game?.gameConfiguration?.operatingStartTime} - {game?.gameConfiguration?.operatingEndTime}
                            </p>
                        </div>
                    </div>
                </div>
            </Card>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                <Card className="col-span-2">
                    <UpcomingSlots 
                        slots={slots}
                        selectedSlot={selectedSlot}
                        onSlotSelect={handleSlotSelect}
                    />
                </Card>

                <Card>
                    <InterestedUsers 
                        users={interestedUsers}
                        selectedUsers={selectedUsers}
                        onUserToggle={handleUserToggle}
                    />

                    {selectedSlot && selectedUsers.length > 0 && (
                        <div className="mt-4 p-4 bg-gray-50 rounded-lg">
                            <p className="text-sm text-gray-600 mb-2">
                                Selected: {selectedUsers.length} user(s) for slot
                            </p>
                            <Button 
                                onClick={handleBookSlot}
                                disabled={bookGameSlot.isPending}
                                className="w-full"
                            >
                                {bookGameSlot.isPending ? 'Booking...' : 'Book Slot'}
                            </Button>
                        </div>
                    )}
                </Card>
            </div>
        </div>
    )
}

export default SingleGame
