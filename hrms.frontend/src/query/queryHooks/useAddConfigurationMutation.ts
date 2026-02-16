import { useMutation, useQueryClient } from '@tanstack/react-query'
import { QUERY_KEY } from '../key';
import { addConfiguration } from '../api/configurations';

const useAddConfigurationMutation = () => {
    const client = useQueryClient();
    return useMutation({
        mutationFn: addConfiguration,
        onSuccess: () => {
            client.invalidateQueries({ queryKey: [QUERY_KEY.CONFIGURATIONS] });
        },
    })
}

export default useAddConfigurationMutation
