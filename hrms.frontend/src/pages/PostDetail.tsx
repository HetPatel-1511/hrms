import { useParams } from 'react-router'
import Loading from '../components/Loading'
import ServerError from '../components/ServerError'
import useSinglePostQuery from '../query/queryHooks/useSinglePostQuery'
import Card from '../components/Card'
import formatDate from '../utils/formatDate'
import UserPill from '../components/UserPill'
import CommentItem from '../components/CommentItem'
import { HandThumbUpIcon as ThumbUpOutline } from '@heroicons/react/24/outline'
import { HandThumbUpIcon as ThumbUpSolid } from '@heroicons/react/24/solid'
import useLikePostMutation from '../query/queryHooks/useLikePostMutation'
import useUnlikePostMutation from '../query/queryHooks/useUnlikePostMutation'
import useAddCommentMutation from '../query/queryHooks/useAddCommentMutation'
import { toast } from 'react-toastify'
import { useAuthorization } from '../hooks/useAuthorization'
import { useState } from 'react'

const PostDetail = () => {
    const { postId } = useParams()
    const { data, isLoading, isError } = useSinglePostQuery(postId)
    const likePost = useLikePostMutation()
    const unlikePost = useUnlikePostMutation()
    const { isOwner } = useAuthorization()
    const [commentText, setCommentText] = useState('')
    const [parentCommentId, setParentCommentId] = useState<number | null>(null)
    const [parentCommentAuthor, setParentCommentAuthor] = useState<string | null>(null)
    const addComment = useAddCommentMutation()

    if (isLoading) return <Loading />
    if (isError) return <ServerError />

    const post = data?.data
    if (!post) return null

    const handleLikeToggle = () => {
        if (post.isLiked) {
            unlikePost.mutate(post.id)
        } else {
            likePost.mutate(post.id)
        }
    }

    const handleAddComment = () => {
        const text = (commentText || '').trim()
        if (!text) {
            toast.error('Comment cannot be empty')
            return
        }
        const data: any = { text }
        if (parentCommentId) data.parentCommentId = parentCommentId
        addComment.mutate({ postId, data }, {
            onSuccess: () => {
                toast.success('Comment added')
                setCommentText('')
                setParentCommentId(null)
                setParentCommentAuthor(null)
            }
        })
    }

    return (
        <main>
            <div className="mb-6">
                <Card className="p-6">
                    <div className="flex items-start justify-between">
                        <div className="flex-1">
                            <h1 className="text-3xl font-bold text-gray-900 mb-2">{post.title}</h1>
                            <div className="flex items-center space-x-4 text-sm text-gray-500">
                                <div>Posted by <UserPill user={{ image: post.author?.profileMedia?.url, name: post.author?.name }} className="ml-2" /></div>
                                <div> • {post.createdAt ? formatDate(post.createdAt, { year: 'numeric', month: 'short', day: 'numeric' }) : ''}</div>
                            </div>
                        </div>
                        <div className="flex items-center space-x-3">
                            <button onClick={handleLikeToggle} className="flex items-center space-x-2 px-3 py-1 rounded hover:bg-gray-100">
                                {post.isLiked ? <ThumbUpSolid className='h-5 w-5 text-indigo-600' /> : <ThumbUpOutline className='h-5 w-5 text-gray-400' />}
                                <span className="text-sm text-gray-700">{post.likeCount ?? 0}</span>
                            </button>
                            {isOwner(post.author?.id) && (
                                <span className="text-sm text-gray-500">You</span>
                            )}
                        </div>
                    </div>
                </Card>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                <div className="lg:col-span-2">
                    <Card className="p-6">
                        <div className="prose max-w-none">
                            <p className="text-gray-700 whitespace-pre-wrap">{post.description}</p>
                        </div>
                        {post.media && post.media.url && post.media.mimeType && post.media.mimeType.startsWith('image') && (
                            <div className="mt-4">
                                <img src={post.media.url} alt={post.media.originalName || 'post media'} className="w-full max-h-96 object-cover rounded" />
                            </div>
                        )}
                        {post.tags && post.tags.length > 0 && (
                            <div className="mt-4 flex flex-wrap gap-2">
                                {post.tags.map((t: any) => (
                                    <span key={t.id || t} className="px-2 py-1 text-xs font-medium rounded-full bg-gray-100 text-gray-700">{t.tag || t}</span>
                                ))}
                            </div>
                        )}
                    </Card>

                    <Card className="p-6 mt-6">
                        <h2 className="text-xl font-semibold mb-4">Comments ({post.commentCount ?? 0})</h2>

                        {/* Add comment UI (design only) */}
                        <div className="mb-4">
                            {parentCommentId && (
                                <div className="mb-2 text-sm">
                                    Replying to <span className="font-medium">{parentCommentAuthor || ('#' + parentCommentId)}</span>
                                    <button className="ml-3 text-xs text-gray-500 hover:underline" onClick={() => { setParentCommentId(null); setParentCommentAuthor(null); }}>Cancel</button>
                                </div>
                            )}
                            <textarea value={commentText} onChange={(e) => setCommentText(e.target.value)} placeholder="Write a comment..." className="w-full border border-gray-300 rounded p-2" rows={3} />
                            <div className="mt-2 text-right">
                                <button
                                    onClick={handleAddComment}
                                    disabled={addComment.isPending}
                                    className="px-4 py-2 bg-indigo-600 text-white rounded cursor-pointer"
                                >
                                    {addComment.isPending ? 'Submitting...' : 'Submit'}
                                </button>
                            </div>
                        </div>

                        {/* Comments list */}
                        {post.comments && post.comments.length > 0 ? (
                            <div>
                                {post.comments.map((c: any) => (
                                    <CommentItem key={c.id} comment={c} onReply={(id: number, author?: string) => { setParentCommentId(id); setParentCommentAuthor(author || null) }} />
                                ))}
                            </div>
                        ) : (
                            <div className="text-sm text-gray-500">No comments yet</div>
                        )}
                    </Card>
                </div>

                <div>
                    <Card className="p-6">
                        <h3 className="text-lg font-semibold mb-3">Post Details</h3>
                        <div className="text-sm text-gray-700 space-y-2">
                            <div>Likes: {post.likeCount ?? 0}</div>
                            <div>Comments: {post.commentCount ?? 0}</div>
                            <div>Posted: {post.createdAt ? formatDate(post.createdAt, { year: 'numeric', month: 'long', day: 'numeric' }) : ''}</div>
                        </div>
                    </Card>
                </div>
            </div>
        </main>
    )
}

export default PostDetail
