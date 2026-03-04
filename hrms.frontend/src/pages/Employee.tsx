import React, { useState, useEffect } from 'react'
import useEmployeesQuery from '../query/queryHooks/useEmployeeQuery'
import UserAvatar from '../components/UserAvatar';
import ServerError from '../components/ServerError';
import Loading from '../components/Loading';
import { SearchHeader } from '../components/SearchHeader';
import { Link, useSearchParams } from 'react-router';
import Card from '../components/Card';
import FormInput from '../components/TextInput';
import { useForm } from 'react-hook-form';
import Select from 'react-select';
import { set } from 'date-fns';
import Button from '../components/Button';
import { useAuthorization } from '../hooks/useAuthorization';
import useRoleQuery from '../query/queryHooks/useRoleQuery';
import useChangeEmployeeRolesMutation from '../query/queryHooks/useChangeEmployeeRolesMutation';

const Employee = () => {
    const [searchParams, setSearchParams] = useSearchParams()
    const initialSearch = searchParams.get('search') || ''
    const [searchInput, setSearchInput] = useState(initialSearch)
    const [debouncedSearch, setDebouncedSearch] = useState(initialSearch)
    const [showRoleSelect, setShowRoleSelect] = useState(null)
    const [selectedRoles, setSelectedRoles] = useState([]);
    const { hasRole } = useAuthorization();

    const { data, isLoading, isError, isSuccess } = useEmployeesQuery(debouncedSearch)
    const { data: rolesData, isLoading: isRolesLoading, error: rolesError } = useRoleQuery();
    const { mutate: changeRoles, isPending: isChangingRoles } = useChangeEmployeeRolesMutation();


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

    const handleRoleEditClick = (employee: any) => {
        setShowRoleSelect(employee.id);
        setSelectedRoles(employee.roles.map((role: any) => { return { label: role.name, value: role.name } }));
    }

    const handleRoleSave = (employee: any) => {
        if (selectedRoles && selectedRoles.length > 0) {
            changeRoles({
                employeeId: employee.id,
                roles: selectedRoles.map((role: any) => role.value)
            });
        } else {
            changeRoles({
                employeeId: employee.id,
                roles: []
            });
        }
        setShowRoleSelect(null);
        setSelectedRoles([]);
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
                            <Card hoverable={true} key={employee.id}>
                                <Link to={`${employee.id}`} className="flex justify-between mt-3 gap-x-6 px-3 py-5">
                                    <div className="flex min-w-0 gap-x-4 w-1/3">
                                        <UserAvatar user={{ image: employee?.profileMedia?.url, name: employee.name }} className="size-12 h-12 w-12 flex-none rounded-full outline -outline-offset-1 outline-black/10" />
                                        <div className="min-w-0 flex-auto">
                                            <p className="text-sm/6 font-semibold">{employee.name}</p>
                                            <p className="mt-1 truncate text-xs/5 text-gray-600">{employee.email}</p>
                                        </div>
                                    </div>
                                    <div className="hidden shrink-0 sm:flex w-1/6 sm:flex-col sm:items-start">
                                        <p className="text-sm/6">{employee.designation.name}</p>
                                        <p className="mt-1 text-xs/5 text-gray-600">Joining date: {employee.joiningDate}</p>
                                    </div>
                                </Link>
                                {hasRole(['HR']) && (showRoleSelect !== employee.id ?
                                    <div className='px-4 py-1 cursor-pointer bg-gray-100' onClick={() => handleRoleEditClick(employee)}>
                                        <div className="flex flex-wrap items-center gap-2">
                                            Roles: {employee.roles.map((role: any) => (
                                                <span key={role.id} className="inline-flex text-xs items-center rounded-full bg-gray-200 px-2 py-0.5 font-medium text-gray-600">
                                                    {role.name}
                                                </span>
                                            ))}
                                        </div>
                                    </div>
                                    : <div className="px-4 py-1 flex px-4 bg-gray-100">
                                        <Select
                                            isMulti={true}
                                            name={"roles"}
                                            id={"roles"}
                                            options={rolesData?.data.map((role: any) => { return { label: role.name, value: role.name } })}
                                            defaultValue={employee.roles.map((role: any) => { return { label: role.name, value: role.name } })}
                                            className="basic-multi-select w-1/3"
                                            onChange={(selectedOptions: any) => {
                                                console.log("Selected roles:", selectedOptions);
                                                setSelectedRoles(selectedOptions);
                                            }}
                                            closeMenuOnSelect={false}
                                        />
                                        <Button
                                            className="ml-2 h-10"
                                            disabled={isChangingRoles}
                                            onClick={() => handleRoleSave(employee)}
                                        >
                                            Save
                                        </Button>
                                    </div>
                                )}
                            </Card>
                        )
                    })}
                </div>
            )}
        </div>
    )
}

export default Employee
