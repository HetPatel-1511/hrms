import { UserIcon } from '@heroicons/react/24/solid'
import React from 'react'
import UserAvatar from './UserAvatar'

const UserPill = ({ className, user }: any) => {
    return (
        <div className={className}>
            <div className="inline-flex items-center gap-2 bg-slate-100 p-1.5 pr-4 rounded-full text-sm font-medium text-slate-900 border border-slate-200">
                <UserAvatar user={user} />
                <span>{user.name}</span>
            </div>

        </div>
    )
}

export default UserPill
