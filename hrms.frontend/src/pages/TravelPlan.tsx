import { UserIcon } from '@heroicons/react/24/solid'
import Button from '../components/Button'
import Loading from '../components/Loading'
import ServerError from '../components/ServerError'
import useTravelPlanQuery from '../query/queryHooks/useTravelPlanQuery'
import { Link } from 'react-router'
import TravelPlanItem from '../components/TravelPlanItem'
import { useAuthorization } from '../hooks/useAuthorization'

const TravelPlan = ({isMy= false}: any) => {
    const { hasRole } = useAuthorization()
    const canAccessAllTravelPlans = (!isMy && hasRole(["HR"]))
    const { data, isLoading, isSuccess, isError, error } = useTravelPlanQuery(canAccessAllTravelPlans)

    if (isLoading) {
        return <Loading />
    }
    if (isError) {
        return <ServerError />
    }
    if (isSuccess) {
        const travelPlans = canAccessAllTravelPlans ? data?.data : data?.data?.travelPlans
        return (
            <div>
                <h1 className='text-2xl font-bold mb-4'>Travel plans</h1>
                {hasRole(["HR"]) && <Button to={"add"}>Add</Button>}
                <div className='mt-6'>
                    {travelPlans && travelPlans.length > 0 ?
                        travelPlans.map((travelPlan: any) => {
                            return (
                                <TravelPlanItem key={travelPlan.id} travelPlan={travelPlan} />
                            )
                        }) :
                        <h1 className='text-xl font-medium'>No Travel Plans</h1>
                    }
                </div>
            </div>
        )
    }
}
export default TravelPlan;
