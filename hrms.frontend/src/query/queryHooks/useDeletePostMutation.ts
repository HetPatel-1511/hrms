import { useMutation, useQueryClient } from '@tanstack/react-query'
import { deletePost } from '../api/posts';
import { QUERY_KEY } from '../key';

const useDeletePostMutation = () => {
    const client = useQueryClient()
    return useMutation({
        mutationFn: ({postId, isHr = false, remarks=null}: any) => deletePost({postId, isHr, remarks}),
        onSuccess: () => {
            client.invalidateQueries({ queryKey: [QUERY_KEY.POSTS] })
        }
    })
}

export default useDeletePostMutation
