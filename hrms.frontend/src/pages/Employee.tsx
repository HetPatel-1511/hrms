import React from 'react'
import useEmployeesQuery from '../query/queryHooks/useEmployeeQuery'
import UserAvatar from '../components/UserAvatar';
import ServerError from '../components/ServerError';
import Loading from '../components/Loading';
import { Link } from 'react-router';
import Card from '../components/Card';

const Employee = () => {
    const { data, isLoading, isError, error, isSuccess } = useEmployeesQuery()
    
    if (isLoading) {
        return <Loading />
    }
    if (isError) {
        return <ServerError />
    }
    if (isSuccess) {
        const employees = data?.data;
        return (
            <div>
                <h1 className='text-2xl font-bold'>Employees</h1>
                <div>
                    {employees?.map((employee: any) => {
                        return (
                            <Card>
                                <Link to={`${employee.id}`} className="flex justify-between mt-3 gap-x-6 px-3 py-5 hover:shadow-lg">
                                    <div className="flex min-w-0 gap-x-4">
                                        <UserAvatar user={{image: employee?.profileMedia?.url, name: employee.name}} className="size-12 h-12 w-12 flex-none rounded-full outline -outline-offset-1 outline-black/10" />
                                        <div className="min-w-0 flex-auto">
                                            <p className="text-sm/6 font-semibold">{employee.name}</p>
                                            <p className="mt-1 truncate text-xs/5 text-gray-600">{employee.email}</p>
                                        </div>
                                    </div>
                                    <div className="hidden shrink-0 sm:flex sm:flex-col sm:items-end">
                                        <p className="text-sm/6">{employee.designation.name}</p>
                                        <p className="mt-1 text-xs/5 text-gray-600">Joining date: {employee.joiningDate}</p>
                                    </div>
                                </Link>
                            </Card>
                        )
                    })}
                </div>
            </div>
        )
    }
}

export default Employee
