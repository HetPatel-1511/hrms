import React from 'react'
import UserAvatar from './UserAvatar'
import { Link } from 'react-router'
import Button from './Button'

const UserItem = ({ travellingEmployee, showButtons=false }: any) => {
    return (
            <>
            <div className="mr-4 grid place-items-center">
                <UserAvatar className="h-12 w-12" user={{ image: travellingEmployee?.profileMedia?.url }} />
            </div>
            <div>
                <div className='flex items-center'>
                    <h6 className="text-slate-800 font-medium">
                        {travellingEmployee.name}
                    </h6>
                    <p className="text-slate-500 text-sm ml-2">
                        ({travellingEmployee.email})
                    </p>
                </div>
                <p className="text-slate-500 text-sm">
                    {travellingEmployee.designation.name}
                </p>
            </div>
            {showButtons && 
            <div className=' flex justify-end w-full'>
                <Button to={`employee/${travellingEmployee.id}/documents`}>Documents</Button>
                <Button to={`employee/${travellingEmployee.id}/expenses`} className="ml-2">Expenses</Button>
            </div>}
            </>
    )
}

export default UserItem
