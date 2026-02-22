import { useMutation, useQueryClient } from '@tanstack/react-query'
import { cancelBooking } from '../api/games';
import { QUERY_KEY } from '../key';

const useCancelBookingMutation = () => {
    const client = useQueryClient();
    return useMutation({
        mutationFn: cancelBooking,
        onSuccess: () => {
            client.invalidateQueries({ queryKey: [QUERY_KEY.GAMES] });
        },
    })
}

export default useCancelBookingMutation
