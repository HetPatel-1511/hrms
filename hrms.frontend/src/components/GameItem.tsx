import Card from './Card'
import { useAuthorization } from '../hooks/useAuthorization'
import { Link } from 'react-router'
import { PencilIcon } from '@heroicons/react/24/outline'

const GameItem = ({game}: any) => {
    const { hasRole } = useAuthorization()
    return (
        <Card hoverable={true} className="mt-2">
            <div className="px-4 py-4 sm:px-6">
                <div className="flex items-center justify-between">
                    <h3 className="text-lg font-medium text-indigo-600">
                        {game.name}
                    </h3>
                    <div className="flex items-center space-x-2">
                        <span className={`px-3 py-1 text-xs font-medium rounded-full ${
                            game.active ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                        }`}>
                            {game.active ? 'Active' : 'Inactive'}
                        </span>
                        {hasRole(["HR"]) && (
                            <div className='flex justify-end mr-0'>
                                <Link 
                                    to={`/games/${game.id}/edit`}
                                    className="flex cursor-pointer items-center mt-1 hover:bg-gray-100 p-1 rounded"
                                >
                                    <PencilIcon className='h-4 w-4 text-gray-500' />
                                </Link>
                            </div>
                        )}
                    </div>
                </div>
                {game.gameConfiguration && (
                    <div className="mt-3 text-sm text-gray-500">
                        <div className="">
                            <p>Slot Duration: {game.gameConfiguration.slotDurationMinutes} min</p>
                            <p>Max Players: {game.gameConfiguration.maxPlayersPerSlot}</p>
                            <p>Operating: {game.gameConfiguration.operatingStartTime} - {game.gameConfiguration.operatingEndTime}</p>
                            {hasRole(["HR"]) && <p>Slot Release: {game.gameConfiguration.slotReleaseTime}</p>}
                        </div>
                    </div>
                )}
            </div>
        </Card>
    )
}

export default GameItem
