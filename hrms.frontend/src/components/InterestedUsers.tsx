import UserAvatar from './UserAvatar'

const InterestedUsers = ({ users, selectedUsers, onUserToggle }: any) => {
    return (
        <div className="px-6 py-4">
            <h2 className="text-lg font-semibold text-gray-900 mb-4">
                Interested Users ({users.length})
            </h2>
            <div className="space-y-2 max-h-96 overflow-y-auto">
                {users.map((user: any) => (
                    <div
                        key={user.id}
                        onClick={() => onUserToggle(user.id.toString())}
                        className={`flex items-center p-3 rounded-lg border cursor-pointer ${
                            selectedUsers.includes(user.id.toString())
                                ? 'border-indigo-500'
                                : 'border-gray-200'
                        }`}
                    >
                        <UserAvatar 
                            user={{ image: user.profileMedia?.url }} 
                            className="h-8 w-8 mr-3" 
                        />
                        <div className="flex-1">
                            <p className="font-medium text-gray-900">{user.name}</p>
                            <p className="text-sm text-gray-500">{user.email}</p>
                        </div>
                    </div>
                ))}
                {users.length === 0 && (
                    <p className="text-gray-500 text-center py-8">No interested users</p>
                )}
            </div>
        </div>
    )
}

export default InterestedUsers
