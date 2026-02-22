import { useSelector } from 'react-redux'
import { selectUser } from '../redux/slices/userSlice'
import { useParams } from 'react-router'
import Card from '../components/Card'
import UserAvatar from '../components/UserAvatar'
import useSingleEmployeeQuery from '../query/queryHooks/useSingleEmployeeQuery'
import useGamesQuery from '../query/queryHooks/useGamesQuery'
import useUserInterestedGamesQuery from '../query/queryHooks/useUserInterestedGamesQuery'
import useUpdateGameInterestMutation from '../query/queryHooks/useUpdateGameInterestMutation'
import Loading from '../components/Loading'
import ServerError from '../components/ServerError'

const UserProfile = () => {
    const { id } = useParams()
    const currentUser = useSelector(selectUser)
    const { data, isLoading, isError } = useSingleEmployeeQuery(id)
    const { data: gamesData, isLoading: gamesLoading } = useGamesQuery()
    const { data: interestedGamesData, isLoading: interestedLoading } = useUserInterestedGamesQuery(id!)
    const updateGameInterest = useUpdateGameInterestMutation()

    if (isLoading || gamesLoading || interestedLoading) {
        return <Loading />
    }
    if (isError) {
        return <ServerError />
    }

    const employee = data?.data
    const games = gamesData?.data || []
    const interestedGames = interestedGamesData?.data || []
    const isOwnProfile = currentUser?.id === parseInt(id || '0')

    const handleToggleInterest = (gameId: string) => {
        updateGameInterest.mutate(gameId)
    }

    const isGameInterested = (gameId: number) => {
        return interestedGames.some((game: any) => game.id === gameId)
    }

    return (
        <div>
            <div className="bg-white shadow-sm rounded-lg">
                <div className="px-4 py-5 sm:px-6">
                    <h3 className="text-lg leading-6 font-medium text-gray-900">
                        Employee Profile
                        {isOwnProfile && <span className="ml-2 text-sm text-gray-500">(You)</span>}
                    </h3>
                </div>
                <div className="border-t border-gray-200">
                    <dl>
                        <div className="bg-gray-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                            <dt className="text-sm font-medium text-gray-500">Photo</dt>
                            <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                                <UserAvatar 
                                    user={{ image: employee?.profileMedia?.url }} 
                                    className="h-20 w-20"
                                />
                            </dd>
                        </div>
                        <div className="bg-white px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                            <dt className="text-sm font-medium text-gray-500">Full name</dt>
                            <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                                {employee?.name}
                            </dd>
                        </div>
                        <div className="bg-gray-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                            <dt className="text-sm font-medium text-gray-500">Email address</dt>
                            <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                                {employee?.email}
                            </dd>
                        </div>
                        <div className="bg-white px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                            <dt className="text-sm font-medium text-gray-500">Designation</dt>
                            <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                                {employee?.designation?.name}
                            </dd>
                        </div>
                        <div className="bg-gray-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                            <dt className="text-sm font-medium text-gray-500">Roles</dt>
                            <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                                {employee?.roles?.map((role: any, index: number) => (
                                    <span key={index} className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-indigo-100 text-indigo-800 mr-2 mb-2">
                                        {role.name}
                                    </span>
                                ))}
                            </dd>
                        </div>
                        <div className="bg-white px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                            <dt className="text-sm font-medium text-gray-500">Manager</dt>
                            <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                                {employee?.manager ? (
                                    <span>{employee.manager.name} ({employee.manager.email})</span>
                                ) : (
                                    <span className="text-gray-400">No manager assigned</span>
                                )}
                            </dd>
                        </div>
                        <div className="bg-white px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                            <dt className="text-sm font-medium text-gray-500">Employee ID</dt>
                            <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                                {employee?.id || 'N/A'}
                            </dd>
                        </div>
                    </dl>
                </div>
            </div>
            
            <div className="bg-white shadow-sm rounded-lg mt-6">
                <div className="px-4 py-5 sm:px-6">
                    <h3 className="text-lg leading-6 font-medium text-gray-900">
                        Game Interests
                    </h3>
                </div>
                <div className="border-t border-gray-200">
                    <div className="px-4 py-5 sm:px-6">
                        <div className="space-y-4">
                            {games.map((game: any) => (
                                <div key={game.id} className="flex items-center justify-between p-4 border border-gray-200 rounded-lg">
                                    <div className="flex items-center space-x-3">
                                        <div className="flex-shrink-0">
                                            <div className="w-10 h-10 bg-indigo-100 rounded-full flex items-center justify-center">
                                                <span className="text-indigo-600 font-semibold">
                                                    {game.name.charAt(0).toUpperCase()}
                                                </span>
                                            </div>
                                        </div>
                                        <div>
                                            <h4 className="text-sm font-medium text-gray-900">{game.name}</h4>
                                            <p className="text-sm text-gray-500">
                                                {game.active ? 'Active' : 'Inactive'} • 
                                                {game.gameConfiguration && ` ${game.gameConfiguration.slotDurationMinutes}min slots`}
                                            </p>
                                        </div>
                                    </div>
                                    {isOwnProfile ? (
                                        <button
                                            onClick={() => handleToggleInterest(game.id.toString())}
                                            disabled={!game.active || updateGameInterest.isPending}
                                            className={`relative inline-flex h-6 w-11 flex-shrink-0 cursor-pointer rounded-full border-2 border-transparent transition-colors duration-200 ease-in-out focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 ${
                                                isGameInterested(game.id) ? 'bg-indigo-600' : 'bg-gray-200'
                                            } ${(!game.active || updateGameInterest.isPending) ? 'opacity-50 cursor-not-allowed' : ''}`}
                                        >
                                            <span
                                                className={`pointer-events-none inline-block h-5 w-5 transform rounded-full bg-white shadow ring-0 transition duration-200 ease-in-out ${
                                                    isGameInterested(game.id) ? 'translate-x-5' : 'translate-x-0'
                                                }`}
                                            />
                                        </button>
                                    ) : (
                                        <div className="flex items-center">
                                            {isGameInterested(game.id) ? (
                                                <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
                                                    Interested
                                                </span>
                                            ) : (
                                                <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-gray-100 text-gray-800">
                                                    Not Interested
                                                </span>
                                            )}
                                        </div>
                                    )}
                                </div>
                            ))}
                            {games.length === 0 && (
                                <p className="text-gray-500 text-center py-4">No games available</p>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default UserProfile
