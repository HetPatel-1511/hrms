import React from 'react'
import { Link } from 'react-router'
import Card from './Card'
import formatDate from '../utils/formatDate'
import UserPill from './UserPill'
import { DocumentIcon } from '@heroicons/react/24/solid'
import { useAuthorization } from '../hooks/useAuthorization'
import { PencilIcon } from '@heroicons/react/24/outline'
import { HandThumbUpIcon as ThumbUpOutline } from '@heroicons/react/24/outline'
import { HandThumbUpIcon as ThumbUpSolid } from '@heroicons/react/24/solid'
import useLikePostMutation from '../query/queryHooks/useLikePostMutation'
import useUnlikePostMutation from '../query/queryHooks/useUnlikePostMutation'

const PostItem = ({ post }: any) => {
    const { isOwner } = useAuthorization()
    const likePost = useLikePostMutation()
    const unlikePost = useUnlikePostMutation()

    const handleLikeToggle = (e: any) => {
        e.stopPropagation()
        e.preventDefault()
        if (post.isLiked) {
            unlikePost.mutate(post.id)
        } else {
            likePost.mutate(post.id)
        }
    }

    return (
        <Card hoverable={true} className="mt-2">
            <Link to={`/post/${post.id}`} className="hover:bg-gray-50">
                <div className="px-4 py-4 sm:px-6">
                    <div className="flex items-center justify-between">
                        <h3 className="text-lg font-medium text-indigo-600">
                            {post.title}
                        </h3>
                        <div className=" items-center space-x-2">
                            {isOwner(post.author?.id) && (
                                <div className='flex justify-end mr-0'>
                                    <Link
                                        to={`/post/${post.id}/edit`}
                                        className="flex cursor-pointer items-center mt-1 hover:bg-gray-100 p-1 rounded"
                                        onClick={(e) => e.stopPropagation()}
                                    >
                                        <PencilIcon className='h-4 w-4 text-gray-500' />
                                    </Link>
                                </div>
                            )}
                            <button
                                onClick={handleLikeToggle}
                                className="flex items-center space-x-1 p-1 rounded cursor-pointer hover:bg-gray-100"
                                aria-pressed={post.isLiked}
                                aria-label={post.isLiked ? 'Unlike' : 'Like'}
                            >
                                {post.isLiked ? <ThumbUpSolid className="h-5 w-5 text-indigo-600" /> : <ThumbUpOutline className="h-5 w-5 text-gray-400" />}
                            </button>
                            <span className={`py-1 text-xs font-medium rounded-full text-gray-500`}>
                                By: {post.author?.name}
                            </span>
                        </div>
                    </div>
                    <p className="mt-2 text-gray-600 truncate w-full">
                        {post.description}
                    </p>
                    {post.media && post.media.url && (
                        post.media.mimeType && post.media.mimeType.startsWith("image") && (
                            <div className="mt-3">
                                <img src={post.media.url} alt={post.media.originalName || 'post media'} className="max-w-lg max-h-lg object-cover rounded" />
                            </div>
                        )
                    )}
                    {post.tags && post.tags.length > 0 && (
                        <div className="mt-3 flex flex-wrap gap-2">
                            Tags: {post.tags.map((tag: any) => (
                                <span key={tag.id || tag} className="px-2 py-1 text-xs font-medium rounded-full bg-gray-100 text-gray-700">
                                    {tag.tag || tag}
                                </span>
                            ))}
                        </div>
                    )}
                    <div className="mt-3 flex items-center justify-between text-sm text-gray-500">
                        <span>
                            {post.likeCount ?? 0} Likes • {post.commentCount ?? 0} Comments
                        </span>
                        <span>
                            {formatDate(post.createdAt, { year: 'numeric', month: 'short', day: 'numeric' })}
                        </span>
                    </div>
                </div>
            </Link>
        </Card>
    )
}

export default PostItem
