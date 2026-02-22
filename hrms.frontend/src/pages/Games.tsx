import Button from '../components/Button'
import Loading from '../components/Loading'
import ServerError from '../components/ServerError'
import useGamesQuery from '../query/queryHooks/useGamesQuery'
import { useAuthorization } from '../hooks/useAuthorization'
import GameItem from '../components/GameItem'

const Games = () => {
    const { hasRole } = useAuthorization()
    const { data, isLoading, isSuccess, isError } = useGamesQuery()

    if (isLoading) {
        return <Loading />
    }
    if (isError) {
        return <ServerError />
    }
    if (isSuccess) {
        const games = data?.data
        return (
            <div>
                <h1 className='text-2xl font-bold mb-4'>Games</h1>
                {hasRole(["HR"]) && <Button to={"add"}>Add</Button>}
                <div className='mt-6'>
                    {games && games.length > 0 ?
                        games.map((game: any) => {
                            return (
                                <GameItem key={game.id} game={game} />
                            )
                        }) :
                        <h1 className='text-xl font-medium'>No Games</h1>
                    }
                </div>
            </div>
        )
    }
}

export default Games;
