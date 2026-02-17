import formatDate from '../utils/formatDate'
import Card from './Card'
import { Link } from 'react-router';

const JobOpeningItem = ({ jobOpening }: any) => {
    return (
        <Link to={`/job-openings/${jobOpening.id}`}>
            <Card hoverable={true} className="mt-3">
                <div className="px-4 py-4 sm:px-6">
                    <div className="flex items-center justify-between">
                        <h3 className="text-lg font-medium text-indigo-600">
                            {jobOpening.title}
                        </h3>
                        <div className="flex items-center space-x-2">
                            <span className={`px-3 py-1 text-xs font-medium rounded-full ${jobOpening.isActive ? 'text-green-600' : 'text-gray-500'}`}>
                                {jobOpening.isActive ? 'Active' : 'Inactive'}
                            </span>
                        </div>
                    </div>
                    <p className="mt-2 text-gray-600 truncate w-full">
                        {jobOpening.summary}
                    </p>
                    <div className="mt-3 flex items-center justify-between text-sm text-gray-500">
                        <span>Posted by: {jobOpening.hr?.name}</span>
                        {/* <span>
                            Created At {formatDate(jobOpening.createdAt, {
                                year: 'numeric',
                                month: 'short',
                                day: 'numeric'
                            })}
                        </span> */}
                    </div>
                    {/* <div className="mt-4 flex space-x-2">
                        <Button
                            onClick={onShare}
                            className="text-sm"
                        >
                            Share
                        </Button>
                        <Button
                            onClick={onRefer}
                            className="text-sm"
                        >
                            Refer
                        </Button>
                    </div> */}
                </div>
            </Card>
        </Link>
    )
}

export default JobOpeningItem
