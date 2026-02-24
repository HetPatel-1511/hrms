import { useMutation, useQueryClient } from '@tanstack/react-query'
import { markNotificationAsRead } from '../api/notifications';
import { QUERY_KEY } from '../key';

const useMarkNotificationAsReadMutation = () => {
    const client = useQueryClient();
    return useMutation({
        mutationFn: (notificationId: any) => markNotificationAsRead(notificationId),
        onSuccess: () => {
            client.invalidateQueries({ queryKey: [QUERY_KEY.NOTIFICATIONS_UNREAD] });
        }
    })
}

export default useMarkNotificationAsReadMutation
