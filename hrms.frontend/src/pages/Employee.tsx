import React, { useState, useEffect } from 'react'
import useEmployeesQuery from '../query/queryHooks/useEmployeeQuery'
import UserAvatar from '../components/UserAvatar';
import ServerError from '../components/ServerError';
import Loading from '../components/Loading';
import { SearchHeader } from '../components/SearchHeader';
import { Link, useSearchParams } from 'react-router';
import Card from '../components/Card';

const Employee = () => {
    const [searchParams, setSearchParams] = useSearchParams()
    const initialSearch = searchParams.get('search') || ''
    const [searchInput, setSearchInput] = useState(initialSearch)
    const [debouncedSearch, setDebouncedSearch] = useState(initialSearch)

    const { data, isLoading, isError, isSuccess } = useEmployeesQuery(debouncedSearch)

    useEffect(() => {
        const timer = setTimeout(() => {
            setDebouncedSearch(searchInput)
            if (searchInput.trim()) {
                searchParams.set('search', searchInput)
            } else {
                searchParams.delete('search')
            }
            setSearchParams(searchParams)
        }, 500)

        return () => clearTimeout(timer)
    }, [searchInput, searchParams, setSearchParams])

    useEffect(() => {
        setSearchInput(initialSearch)
        setDebouncedSearch(initialSearch)
    }, [initialSearch])

    const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSearchInput(e.target.value)
    }
    
    if (isError) {
        return <ServerError />
    }
    
    return (
        <div>
            {(isLoading || isSuccess) && (
                <SearchHeader 
                    title="Employees"
                    searchValue={searchInput}
                    onSearchChange={handleSearchChange}
                    placeholder="Search employees..."
                />
            )}
            
            {isLoading && <Loading />}
            
            {isSuccess && (
                <div>
                    {data?.data?.map((employee: any) => {
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
            )}
        </div>
    )
}

export default Employee
