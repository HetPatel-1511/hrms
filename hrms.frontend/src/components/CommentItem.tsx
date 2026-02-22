import React from 'react'
import UserAvatar from './UserAvatar'
import formatDate from '../utils/formatDate'

const CommentItem = ({ comment, depth = 0, onReply }: any) => {
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
                        <div className="text-xs text-gray-500">{comment.createdAt ? formatDate(comment.createdAt, { year: 'numeric', month: 'short', day: 'numeric' }) : ''}</div>
                    </div>
                    <div className="text-sm text-gray-700 mt-1 whitespace-pre-wrap">{comment.text}</div>
                    {comment.replies && comment.replies.length > 0 && (
                        <div className="mt-2">
                            {comment.replies.map((r: any) => (
                                <CommentItem key={r.id} comment={r} depth={depth + 1} onReply={onReply} />
                            ))}
                        </div>
                    )}
                </div>
            </div>
        </div>
    )
}

export default CommentItem
