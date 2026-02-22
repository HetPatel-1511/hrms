import { useMutation, useQueryClient } from '@tanstack/react-query'
import { addPost } from '../api/posts';
import { QUERY_KEY } from '../key';

const useAddPostMutation = () => {
    const client = useQueryClient();
    return useMutation({
        mutationFn: addPost,
        onSuccess: () => {
            client.invalidateQueries({ queryKey: [QUERY_KEY.POSTS] });
        },
    })
}

export default useAddPostMutation
