import { useMutation } from '@tanstack/react-query'
import { shareJob } from '../api/jobOpening';

const useShareJobMutation = () => {
    return useMutation({
        mutationFn: shareJob,
    })
}

export default useShareJobMutation
