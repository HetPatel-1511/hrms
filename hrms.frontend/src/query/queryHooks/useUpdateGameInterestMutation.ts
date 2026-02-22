import { useMutation, useQueryClient } from '@tanstack/react-query'
import { updateGameInterest } from '../api/games';
import { QUERY_KEY } from '../key';

const useUpdateGameInterestMutation = () => {
    const client = useQueryClient();
    return useMutation({
        mutationFn: updateGameInterest,
        onSuccess: () => {
            client.invalidateQueries({ queryKey: [QUERY_KEY.GAMES] });
        },
    })
}

export default useUpdateGameInterestMutation
