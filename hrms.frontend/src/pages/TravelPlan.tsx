import { UserIcon } from '@heroicons/react/24/solid'
import Button from '../components/Button'
import Loading from '../components/Loading'
import ServerError from '../components/ServerError'
import useTravelPlanQuery from '../query/queryHooks/useTravelPlanQuery'
import { Link } from 'react-router'
import TravelPlanItem from '../components/TravelPlanItem'

const TravelPlan = () => {
    const { data, isLoading, isSuccess, isError, error } = useTravelPlanQuery()
    if (isLoading) {
        return <Loading />
    }
    if (isError) {
        return <ServerError />
    }
    if (isSuccess) {
        return (
            <div>
                <h1 className='text-2xl font-bold mb-4'>Travel plans</h1>
                <Button to={"add"}>Add</Button>
                <div className='mt-6'>
                    {data.data && data.data.length > 0 ?
                        data.data.map((travelPlan: any) => {
                            return (
                                <TravelPlanItem key={travelPlan.id} travelPlan={travelPlan} />
                            )
                        }) :
                        <h1 className='text-4xl font-medium'>No Travel Positions</h1>
                    }
                </div>
            </div>
        )
    }
}
export default TravelPlan;
