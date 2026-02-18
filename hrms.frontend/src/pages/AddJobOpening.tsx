import { useEffect, useState } from 'react'
import Button from '../components/Button'
import { useForm } from 'react-hook-form';
import FormInput from '../components/TextInput';
import useAddJobOpeningMutation from '../query/queryHooks/useAddJobOpeningMutation';
import useEmployeesQuery from '../query/queryHooks/useEmployeeQuery';
import useEmployeesByRoleQuery from '../query/queryHooks/useEmployeesByRoleQuery';
import { useNavigate } from 'react-router';

const AddJobOpening = () => {
    const navigate = useNavigate();
    const [selectedCvReviewers, setSelectedCvReviewers] = useState([]);
    const {
        register,
        handleSubmit,
        formState: { errors },
        setValue
    } = useForm();

    const addJobOpening = useAddJobOpeningMutation()
    const { data: employeesData } = useEmployeesQuery()
    const { data: hrData } = useEmployeesByRoleQuery('HR')
    
    const onSubmit = (data: any) => {
        data = { 
            ...data, 
            descriptionMedia: data.descriptionMedia[0],
            cvReviewerIds: selectedCvReviewers.map((reviewer: any) => reviewer.id)
        };
        addJobOpening.mutate(data);
    };

    useEffect(() => {
        if (addJobOpening.isSuccess) {
            navigate('/job-openings')
        }
    }, [addJobOpening.isSuccess])

    const employeeOptions = employeesData?.data?.map((employee: any) => ({
        value: employee.email,
        label: employee.name+` (${employee.email})`,
        id: employee.id
    })) || [];

    const hrOptions = hrData?.data?.map((hr: any) => ({
        value: hr.id,
        label: hr.name
    })) || [];

    const onCvReviewersChange = (value: any) => {
        setValue("cvReviewerIds", value);
        setSelectedCvReviewers(value);
    };

    return (
        <div>
            <h1 className='text-2xl mb-4'>Add Job Opening</h1>
            <form className="">
                <div className="mb-5">
                    <FormInput
                        label="Title"
                        id="title"
                        placeholder="Job Title"
                        register={register}
                        errors={errors}
                        validation={{ required: 'Title is mandatory' }}
                    />
                </div>
                <div className="mb-5">
                    <FormInput
                        label="Summary"
                        id="summary"
                        placeholder="Job Summary"
                        register={register}
                        errors={errors}
                        validation={{ required: 'Summary is mandatory' }}
                    />
                </div>
                <div className="mb-5">
                    <FormInput
                        type="multi-select"
                        label="CV Reviewers"
                        id="cvReviewerIds"
                        register={register}
                        errors={errors}
                        options={employeeOptions}
                        onChange={onCvReviewersChange}
                        isMult={true}
                    />
                </div>
                <div className="mb-5">
                    <FormInput
                        type="select"
                        label="HR"
                        id="hrId"
                        register={register}
                        errors={errors}
                        options={hrOptions}
                        validation={{ required: 'HR is mandatory' }}
                    />
                </div>
                <div className="mb-5">
                    <FormInput
                        type="file"
                        label="Job Description Document"
                        id="descriptionMedia"
                        placeholder=""
                        register={register}
                        errors={errors}
                        validation={{ required: 'Job description document is mandatory' }}
                    />
                </div>
                <Button onClick={handleSubmit(onSubmit)}>
                    Submit
                </Button>
            </form>
        </div>
    )
}

export default AddJobOpening
