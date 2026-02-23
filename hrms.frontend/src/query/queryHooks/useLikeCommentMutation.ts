import { useMutation, useQueryClient } from '@tanstack/react-query'
import { likeComment } from '../api/posts';
import { QUERY_KEY } from '../key';

const useLikeCommentMutation = () => {
    const client = useQueryClient()
    return useMutation({
        mutationFn: ({ postId, commentId }: any) => likeComment({ postId, commentId }),
        onSuccess: () => {
            client.invalidateQueries({ queryKey: [QUERY_KEY.POSTS] })
        }
    })
}

export default useLikeCommentMutation
