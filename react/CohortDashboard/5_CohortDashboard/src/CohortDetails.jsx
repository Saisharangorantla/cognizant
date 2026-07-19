import styles from './CohortDetails.module.css';

function CohortDetails({ cohort }) {
  const titleClass = cohort.status === 'ongoing' ? styles.ongoing : styles.completed;

  return (
    <div className={styles.box}>
      <h3 className={titleClass}>{cohort.name}</h3>
      <dl>
        <dt>Trainer</dt>
        <dd>{cohort.trainer}</dd>
        <dt>Start Date</dt>
        <dd>{cohort.startDate}</dd>
        <dt>End Date</dt>
        <dd>{cohort.endDate}</dd>
        <dt>Status</dt>
        <dd>{cohort.status}</dd>
      </dl>
    </div>
  );
}

export default CohortDetails;
