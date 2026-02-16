import React from 'react'
import { Link, useParams } from 'react-router'
import useEmployeeTravelPlanDocumentsQuery from '../query/queryHooks/useEmployeeTravelPlanDocumentsQuery'
import Loading from '../components/Loading'
import ServerError from '../components/ServerError'
import UserAvatar from '../components/UserAvatar'
import UserItem from '../components/UserItem'
import formatDate from '../utils/formatDate'
import FormInput from '../components/TextInput'
import { useForm } from 'react-hook-form'
import Button from '../components/Button'
import useAddTravelPlanDocumentMutation from '../query/queryHooks/useAddTravelPlanDocumentMutation'

const TravelPlanEmployeeDocuments = () => {
    const { travelPlanId, employeeId } = useParams()
    const { data, isSuccess, isLoading, isError, error } = useEmployeeTravelPlanDocumentsQuery(travelPlanId, employeeId)

    if (isLoading) {
        return <Loading />
    }
    if (isError) {
        return <ServerError />
    }
    if (isSuccess) {
        const travellingEmployee = data?.data.employee;
        const travelDocuments = data?.data.travelDocuments;
        const travelPlan = data?.data.travelPlan;
        const documentTypes = data?.data.documentTypes;
        return (
            <div>
                <Link
                    to={`/travel-plan/${travelPlanId}`}
                    className="inline-flex items-center text-sm text-gray-500 hover:text-gray-700 mb-4"
                >
                    ← Back to travel plan
                </Link>
                <div
                    className="text-slate-800 flex w-full items-center rounded-md p-3 transition-all hover:bg-slate-100 focus:bg-slate-100 active:bg-slate-100"
                >
                    <UserItem travellingEmployee={travellingEmployee} key={travellingEmployee.id} />
                </div>
                <h1 className='text-2xl font-bold px-4 mt-2'>Documents for travelling to {travelPlan.place} <span className='text-sm text-slate-600'>({travelPlan.startDate} to {travelPlan.endDate})</span></h1>
                <div className="p-4">
                    {documentTypes.map((documentType: any) => {
                        const travelDocument = travelDocuments.find((t: any) => t.documentType.name == documentType?.name);
                        return <div className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-xl transition-shadow duration-300 mb-4">
                            <div className="p-4">
                                <h3 className="text-xl mb-2 font-semibold text-gray-900">{documentType.name}</h3>
                                {travelDocument != null ?
                                    <>
                                        <p className="text-sm text-gray-600">Owner: {travelDocument.ownerType.charAt(0).toUpperCase() + travelDocument.ownerType.slice(1).toLowerCase()}</p>
                                        <p className="text-sm text-gray-600">Uploaded: {formatDate(travelDocument?.uploadedAt, {
                                            year: 'numeric',
                                            month: 'short',
                                            day: 'numeric'
                                        })}</p>
                                        <a href={travelDocument?.media?.url} className="w-full h-full text-blue-800 object-cover">{travelDocument?.media?.originalName}</a>
                                    </> :
                                    <>
                                        <DocumentUpload
                                            travelPlanId={travelPlanId}
                                            employeeId={employeeId}
                                            documentType={documentType}
                                        />
                                    </>
                                }
                            </div>
                        </div>
                    }
                    )}
                </div>

            </div>
        )
    }
}

const DocumentUpload = ({ travelPlanId, employeeId, documentType }: any) => {
    const { register, handleSubmit, formState: { errors }, setValue } = useForm();
    const addTravelPlanDocument = useAddTravelPlanDocumentMutation()
    const onSubmit = (data: any) => {
        data = { ...data, documentTypeId: documentType.id, file: data.file[0] };
        addTravelPlanDocument.mutate({ travelPlanId, employeeId, data });
    };

    const onChange = (value: any) => {
        setValue(`ownerType`, value.value);
    }
    return <form>
        <FormInput
            type="multi-select"
            label="Owner type"
            options={[{ value: "HR", label: "HR" }, { value: "EMPLOYEE", label: "EMPLOYEE" }]}
            id={`ownerType`}
            register={register}
            errors={errors}
            validation={{ required: 'Owner type is required' }}
            isMult={false}
            onChange={onChange}
        />
        <FormInput
            type="file"
            label={"file"}
            id={`file`}
            placeholder=""
            register={register}
            errors={errors}
            validation={{ required: 'File is mandatory' }}
        />
        <Button onClick={handleSubmit(onSubmit)}>Submit</Button>
    </form>
}

export default TravelPlanEmployeeDocuments
