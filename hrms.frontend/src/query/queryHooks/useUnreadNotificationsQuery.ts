import { useQuery } from '@tanstack/react-query'
import { QUERY_KEY } from '../key'
import { fetchUnreadNotifications } from '../api/notifications'

const useUnreadNotificationsQuery = (enabled: boolean = true) => {
    return useQuery({
        queryKey: [QUERY_KEY.NOTIFICATIONS_UNREAD],
        queryFn: () => fetchUnreadNotifications(),
        enabled
    })
}

export default useUnreadNotificationsQuery
