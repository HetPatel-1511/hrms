import { useMutation, useQueryClient } from '@tanstack/react-query'
import { updateGameConfiguration } from '../api/games';
import { QUERY_KEY } from '../key';

const useUpdateGameConfigurationMutation = () => {
    const client = useQueryClient();
    return useMutation({
        mutationFn: updateGameConfiguration,
        onSuccess: () => {
            client.invalidateQueries({ queryKey: [QUERY_KEY.GAMES] });
        },
    })
}

export default useUpdateGameConfigurationMutation
