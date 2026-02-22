import { useMutation, useQueryClient } from '@tanstack/react-query'
import { addComment } from '../api/posts';
import { QUERY_KEY } from '../key';

const useAddCommentMutation = () => {
    const client = useQueryClient();
    return useMutation({
        mutationFn: addComment,
        onSuccess: () => {
            client.invalidateQueries({ queryKey: [QUERY_KEY.POSTS] });
        }
    })
}

export default useAddCommentMutation
