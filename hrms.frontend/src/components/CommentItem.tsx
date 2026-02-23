import React from 'react'
import UserAvatar from './UserAvatar'
import { TrashIcon, HandThumbUpIcon as ThumbUpOutline } from '@heroicons/react/24/outline'
import { HandThumbUpIcon as ThumbUpSolid } from '@heroicons/react/24/solid'
import { useAuthorization } from '../hooks/useAuthorization'
import useDeleteCommentMutation from '../query/queryHooks/useDeleteCommentMutation'
import { toast } from 'react-toastify'
import formatDate from '../utils/formatDate'
import useLikeCommentMutation from '../query/queryHooks/useLikeCommentMutation'
import useUnlikeCommentMutation from '../query/queryHooks/useUnlikeCommentMutation'

const CommentItem = ({ postId, comment, depth = 0, onReply }: any) => {
    const { isOwner } = useAuthorization()
    const deleteComment = useDeleteCommentMutation()
    const likeComment = useLikeCommentMutation()
    const unlikeComment = useUnlikeCommentMutation()

    const handleDelete = (e: any) => {
        e.stopPropagation()
        e.preventDefault()
        if (!confirm('Are you sure you want to delete this comment?')) return
        deleteComment.mutate({ postId, commentId: comment.id }, {
            onSuccess: (res: any) => {
                toast.success(res.data || 'Comment deleted')
            }
        })
    }

    const handleLikeToggle = (e: any) => {
        e.stopPropagation()
        e.preventDefault()
        if (comment.isLiked) {
            unlikeComment.mutate({ postId, commentId: comment.id })
        } else {
            likeComment.mutate({ postId, commentId: comment.id })
        }
    }

    return (
        <div className={`mt-3 ${depth > 0 ? 'ml-1' : ''}`}>
            <div className="flex items-start space-x-3">
                <UserAvatar user={{ image: comment.employee?.profileMedia?.url, name: comment.employee?.name }} className="h-8 w-8" />
                <div className="flex-1">
                    <div className="flex items-center justify-between">
                        <div className="text-sm font-medium text-gray-900">
                            {comment.employee?.name}
                            <button
                                onClick={(e) => { e.stopPropagation(); onReply && onReply(comment.id, comment.employee?.name) }}
                                className="ml-3 text-xs text-indigo-600 hover:underline"
                                aria-label={`Reply to ${comment.employee?.name}`}
                            >
                                Reply
                            </button>
                        </div>
                        <div className="flex items-center space-x-2">
                            {isOwner(comment.employee?.id) && (
                                <button onClick={handleDelete} className="ml-2 p-1 rounded hover:bg-red-50" aria-label="Delete comment">
                                    <TrashIcon className="h-4 w-4 text-red-500" />
                                </button>
                            )}
                            <button onClick={handleLikeToggle} className="flex items-center space-x-1 px-2 py-1 rounded hover:bg-gray-100" aria-pressed={comment.isLiked} aria-label={comment.isLiked ? 'Unlike comment' : 'Like comment'}>
                                {comment.isLiked ? <ThumbUpSolid className="h-4 w-4 text-indigo-600" /> : <ThumbUpOutline className="h-4 w-4 text-gray-400" />}
                                <span className="text-xs text-gray-500">{comment.likeCount ?? 0}</span>
                            </button>
                            <div className="text-xs text-gray-500">{comment.createdAt ? formatDate(comment.createdAt, { year: 'numeric', month: 'short', day: 'numeric' }) : ''}</div>
                        </div>
                    </div>
                    <div className="text-sm text-gray-700 mt-1 whitespace-pre-wrap">{comment.text}</div>
                    {comment.replies && comment.replies.length > 0 && (
                        <div className="mt-2">
                            {comment.replies.map((r: any) => (
                                <CommentItem key={r.id} postId={postId} comment={r} depth={depth + 1} onReply={onReply} />
                            ))}
                        </div>
                    )}
                </div>
            </div>
        </div>
    )
}

export default CommentItem
