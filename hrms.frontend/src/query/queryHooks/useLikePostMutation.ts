import { useMutation, useQueryClient } from '@tanstack/react-query'
import { likePost } from '../api/posts';
import { QUERY_KEY } from '../key';

const useLikePostMutation = () => {
    const client = useQueryClient();
    return useMutation({
        mutationFn: (postId: any) => likePost(postId),
        onSuccess: () => {
            client.invalidateQueries({ queryKey: [QUERY_KEY.POSTS] });
        }
    })
}

export default useLikePostMutation
