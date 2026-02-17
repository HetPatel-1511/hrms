import React from 'react'
import { Link, useParams } from 'react-router'
import useSingleTravelPlanQuery from '../query/queryHooks/useSingleTravelPlanQuery'
import Loading from '../components/Loading'
import ServerError from '../components/ServerError'
import Card from '../components/Card'
import Button from '../components/Button'
import { ArrowPathIcon, BriefcaseIcon, CalendarIcon, CheckCircleIcon, ClockIcon, EnvelopeIcon, UserIcon, XCircleIcon } from '@heroicons/react/24/solid'
import formatDate from '../utils/formatDate'
import UserPill from '../components/UserPill'
import UserAvatar from '../components/UserAvatar'
import UserItem from '../components/UserItem'

const SingleTravelPlan = () => {
    const { travelPlanId } = useParams()

    const { data, isLoading, isSuccess, isError, error } = useSingleTravelPlanQuery(travelPlanId)

    if (isLoading) {
        return <Loading />
    }
    if (isError) {
        return <ServerError />
    }
    if (isSuccess) {
        const travelPlan = data?.data;

        return (
            <main>
                <div className="mb-8">
                    <Card className="p-6">
                        <div className="flex justify-between items-start justify-between">
                            <div className="flex-1">
                                <h1 className="text-3xl font-bold text-gray-900 mb-2">{travelPlan.place}</h1>
                                <div className="flex items-center space-x-4">
                                    <div className="flex items-center text-sm text-gray-500">
                                        Travel date: {travelPlan.startDate} - {travelPlan.endDate}
                                    </div>
                                </div>
                            </div>
                            <div className="flex items-center text-sm text-gray-500">
                                Created By: <UserPill user={{ image: travelPlan?.createdBy?.profileMedia?.url, name: travelPlan.createdBy.name }} className="ml-2" />
                            </div>
                        </div>
                    </Card>
                </div>

                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                    <div className="lg:col-span-2 space-y-6">
                        <Card className="p-6">
                            <h2 className="text-xl font-semibold text-gray-900 mb-4">Purpose of travel</h2>
                            <div className="prose max-w-none">
                                <p className="text-gray-600 whitespace-pre-wrap">{travelPlan.purpose}</p>
                            </div>
                        </Card>
                        {travelPlan.status === 'closed' && travelPlan.reasonForClosure && (
                            <Card className="p-6">
                                <h2 className="text-xl font-semibold text-gray-900 mb-4">Reason for Closure</h2>
                                <p className="text-gray-600">{travelPlan.reasonForClosure}</p>
                            </Card>
                        )}
                    </div>

                    <div className="space-y-6">
                        {travelPlan.recruiter && (
                            <Card className="p-6">
                                <h2 className="text-xl font-semibold text-gray-900 mb-4">Recruiter Information</h2>
                                <div className="space-y-3">
                                    <div className="flex items-center">
                                        <UserIcon className="h-5 w-5 text-gray-400 mr-3" />
                                        <div>
                                            <p className="text-sm font-medium text-gray-900">{travelPlan.recruiter.name}</p>
                                            {travelPlan.recruiter?.role?.name && <p className="text-sm text-gray-500">{travelPlan.recruiter?.role?.name}</p>}
                                        </div>
                                    </div>
                                    {travelPlan.recruiter.email && (
                                        <div className="flex items-center">
                                            <EnvelopeIcon className="h-5 w-5 text-gray-400 mr-3" />
                                            <div>
                                                <p className="text-sm text-gray-900">{travelPlan.recruiter.email}</p>
                                                <p className="text-sm text-gray-500">Email</p>
                                            </div>
                                        </div>
                                    )}
                                </div>
                            </Card>
                        )}

                        <Card className="p-6">
                            <div className="space-y-3">
                                <div className="flex items-center">
                                    <CalendarIcon className="h-5 w-5 text-gray-400 mr-3" />
                                    <div>
                                        <p className="text-sm text-gray-900">Travel date</p>
                                        <p className="text-sm text-gray-500">{travelPlan.startDate} - {travelPlan.endDate}</p>
                                    </div>
                                </div>
                                <div className="flex items-center">
                                    <CalendarIcon className="h-5 w-5 text-gray-400 mr-3" />
                                    <div>
                                        <p className="text-sm text-gray-900">Created</p>
                                        <p className="text-sm text-gray-500">{travelPlan.createdAt ? formatDate(travelPlan.createdAt, {
                                            year: 'numeric',
                                            month: 'long',
                                            day: 'numeric'
                                        }) : 'Not available'}</p>
                                    </div>
                                </div>
                            </div>
                        </Card>

                    </div>
                </div>

                <div className="mt-8">
                    <Card className="p-6">
                        <div className="flex justify-between items-start justify-between">
                            <div className="flex-1">
                                <h1 className="text-3xl font-bold text-gray-900 mb-2">Travelling employees</h1>
                            </div>
                        </div>
                        <div className="">
                            <div className="relative flex flex-col rounded-lgbg-white">
                                <nav className="flex min-w-[240px] flex-col gap-1 p-1.5">
                                    {travelPlan.travellingEmployees.map((travellingEmployee: any) => (
                                        <div
                                            key={travellingEmployee.id}
                                            
                                            className="text-slate-800 flex w-full items-center rounded-md p-3 cursor-pointer transition-all hover:bg-slate-100 focus:bg-slate-100 active:bg-slate-100"
                                        >
                                            <UserItem employee={travellingEmployee} showButtons={true} />
                                        </div>
                                    ))}
                                </nav>
                            </div>
                        </div>
                    </Card>
                </div>

            </main>
        );
    };
}
export default SingleTravelPlan
