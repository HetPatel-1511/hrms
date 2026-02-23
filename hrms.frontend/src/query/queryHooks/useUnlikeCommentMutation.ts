import { useMutation, useQueryClient } from '@tanstack/react-query'
import { unlikeComment } from '../api/posts';
import { QUERY_KEY } from '../key';

const useUnlikeCommentMutation = () => {
    const client = useQueryClient()
    return useMutation({
        mutationFn: ({ postId, commentId }: any) => unlikeComment({ postId, commentId }),
        onSuccess: () => {
            client.invalidateQueries({ queryKey: [QUERY_KEY.POSTS] })
        }
    })
}

export default useUnlikeCommentMutation
