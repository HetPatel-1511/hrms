import { useMutation, useQueryClient } from '@tanstack/react-query'
import { addJobOpening } from '../api/jobOpening';
import { QUERY_KEY } from '../key';

const useAddJobOpeningMutation = () => {
    const client = useQueryClient();
    return useMutation({
        mutationFn: addJobOpening,
        onSuccess: () => {
            client.invalidateQueries({ queryKey: [QUERY_KEY.JOB_OPENINGS] });
        },
    })
}

export default useAddJobOpeningMutation
