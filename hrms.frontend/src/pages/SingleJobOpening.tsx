import { useParams, Outlet, useNavigate } from 'react-router'
import useSingleJobOpeningQuery from '../query/queryHooks/useSingleJobOpeningQuery'
import Loading from '../components/Loading'
import ServerError from '../components/ServerError'
import Card from '../components/Card'
import Button from '../components/Button'
import { BriefcaseIcon, CalendarIcon, UserIcon, DocumentIcon } from '@heroicons/react/24/solid'
import formatDate from '../utils/formatDate'
import UserPill from '../components/UserPill'
import UserAvatar from '../components/UserAvatar'

const SingleJobOpening = () => {
    const { jobId } = useParams()
    const navigate = useNavigate()
    const { data, isLoading, isSuccess, isError } = useSingleJobOpeningQuery(jobId)

    const handleShare = () => {
        navigate(`/job-openings/${jobId}/share`)
    }

    const handleRefer = () => {
        navigate(`/job-openings/${jobId}/refer`)
    }

    if (isLoading) {
        return <Loading />
    }
    if (isError) {
        return <ServerError />
    }
    if (isSuccess) {
        const jobOpening = data?.data;

        return (
            <>
                <main>
                    <div className="mb-8">
                        <Card className="p-6">
                            <div className="flex justify-between items-start justify-between">
                                <div className="flex-1">
                                    <h1 className="text-3xl font-bold text-gray-900 mb-2">{jobOpening.title}</h1>
                                    <div className="flex items-center space-x-4">
                                        <div className="flex items-center text-sm text-gray-500">
                                            Status: <span className={`ml-2 px-2 py-1 text-xs font-medium rounded-full ${jobOpening.isActive ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'}`}>
                                                {jobOpening.isActive ? 'Active' : 'Inactive'}
                                            </span>
                                        </div>
                                    </div>
                                </div>
                                <div className="flex items-center text-sm text-gray-500">
                                    Posted By: <UserPill user={{ image: jobOpening?.hr?.profileMedia?.url, name: jobOpening.hr?.name }} className="ml-2" />
                                </div>
                            </div>
                        </Card>
                    </div>

                    <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                        <div className="lg:col-span-2 space-y-6">
                            <Card className="p-6">
                                <h2 className="text-xl font-semibold text-gray-900 mb-4">Job Summary</h2>
                                <div className="prose max-w-none">
                                    <p className="text-gray-600 whitespace-pre-wrap">{jobOpening.summary}</p>
                                </div>
                            </Card>
                            {jobOpening.descriptionMedia && (
                                <Card className="p-6">
                                    <h2 className="text-xl font-semibold text-gray-900 mb-4">Job Description Document</h2>
                                    <div className="flex items-center space-x-3">
                                        <DocumentIcon className="h-8 w-8 text-gray-400" />
                                        <div>
                                            <a href={jobOpening.descriptionMedia.url} target="_blank" rel="noopener noreferrer" className="text-blue-600 hover:text-blue-800 font-medium">
                                                {jobOpening.descriptionMedia.originalName}
                                            </a>
                                            <p className="text-sm text-gray-500">Click to view document</p>
                                        </div>
                                    </div>
                                </Card>
                            )}
                            <Card className="p-6">
                                <h2 className="text-xl font-semibold text-gray-900 mb-4">Actions</h2>
                                <div className="flex space-x-4">
                                    <Button onClick={handleShare} className="flex items-center space-x-2">
                                        <span>Share Job</span>
                                    </Button>
                                    <Button onClick={handleRefer} className="flex items-center space-x-2">
                                        <span>Refer Friend</span>
                                    </Button>
                                </div>
                            </Card>
                        </div>

                        <div className="space-y-6">
                            <Card className="p-6">
                                <h2 className="text-xl font-semibold text-gray-900 mb-4">Job Details</h2>
                                <div className="space-y-3">
                                    <div className="flex items-center">
                                        <UserIcon className="h-5 w-5 text-gray-400 mr-3" />
                                        <div>
                                            <p className="text-sm text-gray-900">Posted by</p>
                                            <p className="text-sm text-gray-500">{jobOpening.hr?.name}</p>
                                        </div>
                                    </div>
                                    <div className="flex items-center">
                                        <CalendarIcon className="h-5 w-5 text-gray-400 mr-3" />
                                        <div>
                                            <p className="text-sm text-gray-900">Posted on</p>
                                            <p className="text-sm text-gray-500">{jobOpening.createdAt ? formatDate(jobOpening.createdAt, {
                                                year: 'numeric',
                                                month: 'long',
                                                day: 'numeric'
                                            }) : 'Not available'}</p>
                                        </div>
                                    </div>
                                </div>
                            </Card>
                            {jobOpening.cvReviewers && jobOpening.cvReviewers.length > 0 && (
                                <Card className="p-6">
                                    <h2 className="text-xl font-semibold text-gray-900 mb-4">CV Reviewers</h2>
                                    <div className="space-y-2">
                                        {jobOpening.cvReviewers.map((reviewer: any) => (
                                            <div key={reviewer.id} className="flex items-center space-x-2">
                                                <UserAvatar className="h-8 w-8" user={{ image: reviewer.profileMedia?.url, name: reviewer.name }} />
                                                <span className="text-sm text-gray-700">
                                                    {reviewer.name}
                                                    <span className='text-gray-500'> ({reviewer.email})</span>
                                                </span>
                                            </div>
                                        ))}
                                    </div>
                                </Card>
                            )}
                        </div>
                    </div>
                </main>
                <Outlet context={{ jobOpening }} />
            </>
        );
    };
}

export default SingleJobOpening
