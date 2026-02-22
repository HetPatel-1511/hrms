import Button from '../components/Button'
import Loading from '../components/Loading'
import ServerError from '../components/ServerError'
import usePostsQuery from '../query/queryHooks/usePostsQuery'
import PostItem from '../components/PostItem'
import { useAuthorization } from '../hooks/useAuthorization'

const Posts = () => {
    const { data, isLoading, isSuccess, isError } = usePostsQuery()

    if (isLoading) return <Loading />
    if (isError) return <ServerError />

    if (isSuccess) {
        const posts = data?.data || []
        return (
            <div>
                <h1 className='text-2xl font-bold mb-4'>Posts</h1>
                <Button to={"add"}>Add</Button>
                <div className='mt-6'>
                    {posts && posts.length > 0 ?
                        posts.map((post: any) => (
                            <PostItem key={post.id} post={post} />
                        )) :
                        <h1 className='text-xl font-medium'>No Posts</h1>
                    }
                </div>
            </div>
        )
    }
    return null
}

export default Posts
