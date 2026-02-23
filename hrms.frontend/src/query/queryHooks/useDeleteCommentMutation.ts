import { useMutation, useQueryClient } from '@tanstack/react-query'
import { deleteComment } from '../api/posts';
import { QUERY_KEY } from '../key';

const useDeleteCommentMutation = () => {
    const client = useQueryClient()
    return useMutation({
        mutationFn: ({ postId, commentId }: any) => deleteComment({ postId, commentId }),
        onSuccess: () => {
            client.invalidateQueries({ queryKey: [QUERY_KEY.POSTS] })
        }
    })
}

export default useDeleteCommentMutation
