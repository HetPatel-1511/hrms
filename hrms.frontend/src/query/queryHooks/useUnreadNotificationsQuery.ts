import { useQuery } from '@tanstack/react-query'
import { QUERY_KEY } from '../key'
import { fetchUnreadNotifications } from '../api/notifications'

const useUnreadNotificationsQuery = () => {
    return useQuery({
        queryKey: [QUERY_KEY.NOTIFICATIONS_UNREAD],
        queryFn: () => fetchUnreadNotifications()
    })
}

export default useUnreadNotificationsQuery
