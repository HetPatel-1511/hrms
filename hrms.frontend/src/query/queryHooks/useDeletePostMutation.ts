import { useMutation, useQueryClient } from '@tanstack/react-query'
import { deletePost } from '../api/posts';
import { QUERY_KEY } from '../key';

const useDeletePostMutation = () => {
    const client = useQueryClient()
    return useMutation({
        mutationFn: (postId: any) => deletePost(postId),
        onSuccess: () => {
            client.invalidateQueries({ queryKey: [QUERY_KEY.POSTS], exact: true })
        }
    })
}

export default useDeletePostMutation
