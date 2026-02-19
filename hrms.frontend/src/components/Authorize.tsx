import React from 'react'
import { useAuthorization } from '../hooks/useAuthorization'
import ErrorScreen from './ErrorScreen'
import Navbar from './Navbar'
import { Outlet } from 'react-router'

const Authorize = ({ roles }: { roles: string[] }) => {
    const { canAccess, hasRole, isOwner, user } = useAuthorization()
    if (hasRole(roles)) {
        return <><Outlet /></>
    }
    return (
        <div>
            <Navbar />
            <div className='pt-20'>
                <ErrorScreen status={403} title={"Unauthorized"} description={"You cannot access the resource"} />
            </div>
        </div>
    )
}

export default Authorize
