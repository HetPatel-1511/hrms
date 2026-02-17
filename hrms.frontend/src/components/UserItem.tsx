import React from 'react'
import UserAvatar from './UserAvatar'
import { Link } from 'react-router'
import Button from './Button'

const UserItem = ({ employee, showButtons=false }: any) => {
    return (
            <>
            <div className="mr-4 grid place-items-center">
                <UserAvatar className="h-12 w-12" user={{ image: employee?.profileMedia?.url }} />
            </div>
            <div>
                <div className='flex items-center'>
                    <h6 className="text-slate-800 font-medium">
                        {employee?.name}
                    </h6>
                    <p className="text-slate-500 text-sm ml-2">
                        ({employee?.email})
                    </p>
                </div>
                <p className="text-slate-500 text-sm">
                    {employee?.designation?.name}
                </p>
            </div>
            {showButtons && 
            <div className=' flex justify-end w-full'>
                <Button to={`employee/${employee?.id}/documents`}>Documents</Button>
                <Button to={`employee/${employee?.id}/expenses`} className="ml-2">Expenses</Button>
            </div>}
            </>
    )
}

export default UserItem
