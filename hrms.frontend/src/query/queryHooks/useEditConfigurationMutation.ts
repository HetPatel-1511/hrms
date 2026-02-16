import { useMutation, useQueryClient } from '@tanstack/react-query'
import { QUERY_KEY } from '../key';
import { addConfiguration, editConfiguration } from '../api/configurations';

const useEditConfigurationMutation = () => {
    const client = useQueryClient();
    return useMutation({
        mutationFn: editConfiguration,
        onSuccess: () => {
            client.invalidateQueries({ queryKey: [QUERY_KEY.CONFIGURATIONS] });
        },
    })
}

export default useEditConfigurationMutation
