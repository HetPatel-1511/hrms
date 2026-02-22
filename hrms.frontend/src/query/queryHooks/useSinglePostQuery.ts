import { useQuery } from '@tanstack/react-query'
import { QUERY_KEY } from '../key'
import { fetchSinglePost } from '../api/posts'

const useSinglePostQuery = (id: any) => {
    return useQuery({
        queryKey: [QUERY_KEY.POSTS, id],
        queryFn: () => fetchSinglePost(id),
        enabled: !!id
    })
}

export default useSinglePostQuery
