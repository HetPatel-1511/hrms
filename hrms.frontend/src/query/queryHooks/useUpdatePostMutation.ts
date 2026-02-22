import { useMutation, useQueryClient } from '@tanstack/react-query'
import { updatePost } from '../api/posts';
import { QUERY_KEY } from '../key';

const useUpdatePostMutation = () => {
    const client = useQueryClient();
    return useMutation({
        mutationFn: updatePost,
        onSuccess: () => {
            client.invalidateQueries({ queryKey: [QUERY_KEY.POSTS] });
        },
    })
}

export default useUpdatePostMutation
