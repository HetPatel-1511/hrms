import { useMutation, useQueryClient } from '@tanstack/react-query'
import { unlikePost } from '../api/posts';
import { QUERY_KEY } from '../key';

const useUnlikePostMutation = () => {
    const client = useQueryClient();
    return useMutation({
        mutationFn: (postId: any) => unlikePost(postId),
        onSuccess: () => {
            client.invalidateQueries({ queryKey: [QUERY_KEY.POSTS] });
        }
    })
}

export default useUnlikePostMutation
