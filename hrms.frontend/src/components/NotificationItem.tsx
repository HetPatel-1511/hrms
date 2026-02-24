import React from 'react'
import useMarkNotificationAsReadMutation from '../query/queryHooks/useMarkNotificationAsReadMutation'

const NotificationItem = ({id, title, message, time, isRead}: any) => {
    const markAsReadMutation = useMarkNotificationAsReadMutation();

    const handleMarkAsRead = () => {
        if (!isRead) {
            markAsReadMutation.mutate(id);
        }
    };

    return (
        <div className="flex cursor-default py-3 px-4 border-b ">
            <div className="pl-3 w-full">
                <div className="text-gray-200 font-medium text-md mb-1 dark:text-gray-200">
                    {title}
                </div>
                <div className="text-gray-200 font-normal text-sm mb-1.5 dark:text-gray-200">
                    {message}
                </div>
                <div className="flex justify-between items-center">
                    <div className="text-xs text-gray-400 font-medium text-primary-600 dark:text-primary-500">
                        {time}
                    </div>
                    {!isRead && (
                        <button
                            onClick={handleMarkAsRead}
                            disabled={markAsReadMutation.isPending}
                            className="text-xs text-blue-500 hover:text-blue-400 cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed"
                        >
                            {markAsReadMutation.isPending ? 'Marking...' : 'Mark as read'}
                        </button>
                    )}
                </div>
            </div>
        </div>
    )
}

export default NotificationItem

