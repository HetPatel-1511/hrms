import React from 'react'
import { Link } from 'react-router'
import formatDate from '../utils/formatDate'
import Card from './Card'

const TravelPlanItem = ({travelPlan}: any) => {
    return (
        <Card hoverable={true} className="mt-2">
            <Link to={`/travel-plan/${travelPlan.id}`} className="hover:bg-gray-50">
                <div className="px-4 py-4 sm:px-6">
                    <div className="flex items-center justify-between">
                        <h3 className="text-lg font-medium text-indigo-600">
                            {travelPlan.place}
                        </h3>
                        <div className="flex items-center space-x-2">
                            <span className={`px-3 py-1 text-xs font-medium rounded-full text-gray-500`}>
                                Created By: {travelPlan.createdBy.name}
                            </span>
                        </div>
                    </div>
                    <p className="mt-2 text-gray-600 truncate w-full">
                        {travelPlan.purpose}
                    </p>
                    <div className="mt-3 flex items-center justify-between text-sm text-gray-500">
                        <span>Travel Dates: {travelPlan.startDate} - {travelPlan.endDate}</span>
                        <span>
                            Created At {formatDate(travelPlan.createdAt, {
                                year: 'numeric',
                                month: 'short',
                                day: 'numeric'
                            })}
                        </span>
                    </div>
                </div>
            </Link>
        </Card>
    )
}

export default TravelPlanItem
