import { useMutation, useQueryClient } from '@tanstack/react-query'
import { bookGameSlot } from '../api/games';
import { QUERY_KEY } from '../key';

const useBookGameSlotMutation = () => {
    const client = useQueryClient();
    return useMutation({
        mutationFn: bookGameSlot,
        onSuccess: () => {
            client.invalidateQueries({ queryKey: [QUERY_KEY.GAMES] });
        },
    })
}

export default useBookGameSlotMutation
