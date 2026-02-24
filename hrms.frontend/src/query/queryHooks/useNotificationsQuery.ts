import { useQuery } from '@tanstack/react-query'
import { QUERY_KEY } from '../key'
import { fetchNotifications } from '../api/notifications'

const useNotificationsQuery = () => {
    return useQuery({
        queryKey: [QUERY_KEY.NOTIFICATIONS],
        queryFn: () => fetchNotifications()
    })
}

export default useNotificationsQuery
