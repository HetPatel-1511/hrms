import React from 'react'
import UserAvatar from './UserAvatar'
import { Link } from 'react-router'
import Button from './Button'

const UserItem = ({ employee, showButtons=false }: any) => {
    return (
            <>
            <div className="">
                <UserAvatar className="h-12 w-12 max-w-none mr-2" user={{ image: employee?.profileMedia?.url }} />
            </div>
            <div>
                <div className='flex items-center'>
                    <h6 className="text-slate-800 text-nowrap font-medium">
                        {employee?.name}
                    </h6>
                    <p className="text-slate-500 text-sm ml-2 text-nowrap">
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
