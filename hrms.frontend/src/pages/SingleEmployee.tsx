import useSingleEmployeeQuery from '../query/queryHooks/useSingleEmployeeQuery'
import { Link, useParams } from 'react-router'
import Loading from '../components/Loading'
import ServerError from '../components/ServerError'
import UserItem from '../components/UserItem'
import Card from '../components/Card'

const SingleEmployee = () => {
    const { employeeId } = useParams()
    const { data, isError, isSuccess, isLoading } = useSingleEmployeeQuery(employeeId)

    if (isLoading) {
        return <Loading />
    }
    if (isError) {
        return <ServerError />
    }
    if (isSuccess) {
        const employee = data.data;

        return (
            <div>
                <h1 className='text-2xl font-bold mb-3'>{employee.name} <span className='text-slate-700 text-sm font-normal'>({employee.email})</span></h1>
                <Card className="p-4 mx-auto">
                    <h1 className='text-xl font-bold'>Hierarchy</h1>
                    <TreeNode node={employee} />
                </Card>
                <Card className="mt-4 p-4">
                    <h1 className='text-xl font-bold'>Direct report</h1>
                    {employee.directReports && employee.directReports.length > 0 ?
                        <div className="grid grid-cols-2 mt-3 gap-4">
                            {employee.directReports.map((employee: any) => {
                                return <Link key={employee.id} to={`/employee/${employee.id}`} className="text-slate-800 flex w-full items-center rounded-md p-3 transition-all bg-slate-200 hover:bg-slate-100 focus:bg-slate-100 active:bg-slate-100">
                                    <UserItem employee={employee} />
                                </Link>
                            })}
                        </div>
                        : <p>Noone reports to {employee.name}</p>}
                </Card>
            </div>
        )
    }
}

const TreeNode = ({ node }: any) => {
    const hasChildren = node.manager;

    return (
        <div>
            {/* Recursive call */}
            {hasChildren && (
                <>
                    <TreeNode key={node.id} node={node.manager} />
                    <div className='bg-gray-500 w-0.5 m-auto h-8'></div>
                </>
            )}
            <div className="flex items-center max-w-md mx-auto rounded-md transition duration-150">
                {/* Node Content */}
                <Link to={`/employee/${node.id}`} className="text-slate-800 flex w-full items-center rounded-md p-3 transition-all bg-slate-200 hover:bg-slate-100 focus:bg-slate-100 active:bg-slate-100">
                    <UserItem employee={node} />
                </Link>
            </div>
        </div>
    );
};


export default SingleEmployee;
