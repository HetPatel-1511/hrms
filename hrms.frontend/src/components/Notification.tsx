import React, { useEffect, useState } from 'react'
import NotificationItem from './NotificationItem'
import useUnreadNotificationsQuery from '../query/queryHooks/useUnreadNotificationsQuery'
import { formatDistanceToNow } from 'date-fns'

const Notification = ({notificationList, isLoading, refetch}: any) => {
    const formatTime = (createdAt: string) => {
        try {
            return formatDistanceToNow(new Date(createdAt), { addSuffix: true })
        } catch {
            return 'Recently'
        }
    }

    return (
        <>
            {isLoading ? (
                <div className='flex justify-center items-center py-8'>
                    <span className='text-gray-600 dark:text-gray-400'>Loading...</span>
                </div>
            ) : notificationList.length > 0 ? (
                notificationList.map((notification: any) => (
                    <NotificationItem
                        key={notification.id}
                        id={notification.id}
                        title={notification.title}
                        message={notification.message}
                        time={formatTime(notification.createdAt)}
                        isRead={notification.isRead}
                    />
                ))
            ) : (
                <div className='flex justify-center items-center py-8'>
                    <span className='text-gray-600 dark:text-gray-400'>No notifications</span>
                </div>
            )}
        </>
    )
}

export default Notification
