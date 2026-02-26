import { useQuery } from '@tanstack/react-query'
import { QUERY_KEY } from '../key'
import { fetchPosts } from '../api/posts'

const usePostsQuery = (filters?: any) => {
    return useQuery({
        queryKey: [QUERY_KEY.POSTS, filters],
        queryFn: () => fetchPosts(filters)
    })
}

export default usePostsQuery
