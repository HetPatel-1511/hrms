import { useQuery } from '@tanstack/react-query'
import { QUERY_KEY } from '../key'
import { fetchPosts } from '../api/posts'

const usePostsQuery = () => {
    return useQuery({
        queryKey: [QUERY_KEY.POSTS],
        queryFn: () => fetchPosts()
    })
}

export default usePostsQuery
