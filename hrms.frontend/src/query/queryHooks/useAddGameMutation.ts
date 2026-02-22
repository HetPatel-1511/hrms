import { useMutation, useQueryClient } from '@tanstack/react-query'
import { addGame } from '../api/games';
import { QUERY_KEY } from '../key';

const useAddGameMutation = () => {
    const client = useQueryClient();
    return useMutation({
        mutationFn: addGame,
        onSuccess: () => {
            client.invalidateQueries({ queryKey: [QUERY_KEY.GAMES] });
        },
    })
}

export default useAddGameMutation
