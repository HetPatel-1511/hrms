import React from 'react'
import useConfigurationQuery from '../query/queryHooks/useConfigurationQuery'
import Loading from '../components/Loading'
import ServerError from '../components/ServerError'
import Card from '../components/Card'
import { Link } from 'react-router'
import Button from '../components/Button'
import { PencilIcon } from '@heroicons/react/24/solid'

const Configuration = () => {
    const { data, isLoading, isError, error, isSuccess } = useConfigurationQuery()

    if (isLoading) {
        return <Loading />
    }
    if (isError) {
        return <ServerError />
    }
    if (isSuccess) {
        const configs = data.data;
        return (
            <div>
                <>
                    <h2 className="text-xl font-semibold mb-4 text-gray-800">Configurations</h2>
                    <div className="mb-6">
                        <Button to={"add"}>Add</Button>
                    </div>
                    <div className="grid gap-4 ">
                        <Card className="shadow-lg p-6">
                            {configs.map((config: any) => {
                                return <div className="divide-y divide-gray-200">
                                    <div className="flex items-center justify-between py-3">
                                        <span className="text-gray-500 font-medium">{config.configKey}<p className='text-xs font-normal'>Configured By: {config.updatedBy?.email}</p></span>
                                        <span className="text-gray-700">{config.configValue}</span>
                                        <Button bgColor={"bg-amber-300"} className="flex items-center" to={`${config.configKey}/edit`}><PencilIcon className='w-4 mr-2 h-4' />Edit</Button>
                                    </div>
                                </div>
                            })}
                        </Card>
                    </div>
                </>
            </div>
        )
    }
}

export default Configuration
